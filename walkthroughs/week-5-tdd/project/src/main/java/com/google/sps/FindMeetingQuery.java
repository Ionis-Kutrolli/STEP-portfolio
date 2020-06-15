// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class used to find available meeting times of a query
 */
public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    // Duration of the request
    long requestDuration = request.getDuration();
    // Mandatory attendees for request
    Collection<String> requestAttendees = request.getAttendees();
    // Optional attendees for request
    Collection<String> optionalAttendees = request.getOptionalAttendees();
    // All available meeting options
    Collection<TimeRange> meetingOptions = new ArrayList<>();
    // Events that only optional attendees are attending
    Collection<Event> optionalAttendeeEvents = new ArrayList<>(); 

    if(requestDuration > TimeRange.WHOLE_DAY.duration()){
      return meetingOptions;
    }

    meetingOptions.add(TimeRange.WHOLE_DAY);

    events.forEach(event -> {
      Collection<String> attendees = event.getAttendees();
      if(!Collections.disjoint(attendees, requestAttendees)) {
        removeTime(meetingOptions, event.getWhen(), (int)requestDuration);
      } else if(!Collections.disjoint(attendees, optionalAttendees)) {
        optionalAttendeeEvents.add(event);
      }
    });
    // Check to see if there are times where optional atendees can join change meeting options
    if(!optionalAttendeeEvents.isEmpty()) {
      Collection<TimeRange> options = checkOptionalAttendees(meetingOptions, optionalAttendeeEvents, (int)requestDuration);
      if (!options.isEmpty()){
          return options;
      } else if (requestAttendees.isEmpty()){
        return Arrays.asList();
      }
    }

    return meetingOptions;
  }

  /**
   * Removes the given time from the options by splitting any overlapping times into smaller 
   * segments. Modifies currentOptions. 
   * @param currentOptions The current available options to meet during
   * @param timeToRemove The time to be removed from current options
   * @param durationLimiter The minimum duration of time a time range can be
   */
  private void removeTime(Collection<TimeRange> currentOptions, TimeRange timeToRemove, int durationLimiter) {
    Collection<TimeRange> toRemoveFromList = new ArrayList<>();
    Collection<TimeRange> toAddToList = new ArrayList<>();
    currentOptions.forEach( timeRange -> {
      if(timeRange.overlaps(timeToRemove)) {
        if(timeRange.contains(timeToRemove.start())) {
          TimeRange newTime = TimeRange.fromStartEnd(timeRange.start(), timeToRemove.start(), false);
          if(newTime.duration() >= durationLimiter) {
            toAddToList.add(newTime);
          }
        }
        if(timeRange.contains(timeToRemove.end())) {
          TimeRange newTime = TimeRange.fromStartEnd(timeToRemove.end(), timeRange.end(), false);
          if(newTime.duration() >= durationLimiter) {
            toAddToList.add(newTime);
          }
        }
        toRemoveFromList.add(timeRange);
      }
    });
    currentOptions.removeAll(toRemoveFromList);
    currentOptions.addAll(toAddToList);
  }

  /**
   * Checks to see if all optional attendees can attend by going through events that
   * optional attendees are attending.
   * @param currentOptions The current Time Range options for meeting.
   * @param events The events that optional attendees are attending.
   */
  private Collection<TimeRange> checkOptionalAttendees(Collection<TimeRange> currentOptions, Collection<Event> events, int durationLimiter) {
    Collection<TimeRange> meetingOptions = new ArrayList<>(currentOptions);
    events.forEach(event -> {
      removeTime(meetingOptions, event.getWhen(), durationLimiter);
    });
    return meetingOptions;
  }
}

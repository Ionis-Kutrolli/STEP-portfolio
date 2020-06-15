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

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    long requestDuration = request.getDuration();
    Collection<String> requestAttendees = request.getAttendees();
    Collection<TimeRange> meetingOptions = new ArrayList<>();
    if(requestDuration > TimeRange.WHOLE_DAY.duration()){
      return meetingOptions;
    }
    meetingOptions.add(TimeRange.WHOLE_DAY);
    events.forEach(event -> {
      if (!Collections.disjoint(event.getAttendees(), requestAttendees)) {
        removeTime(meetingOptions, event.getWhen(), (int)requestDuration);
      }
    });
    return meetingOptions;
  }

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
}

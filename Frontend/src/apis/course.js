import apiClient from './apiClient';

export async function getCourses(startDate, endDate) {
  const { data } = await apiClient.get(`/channels/courses?`, {
    params: { startDate, endDate },
  });

  return data.data;
}

export async function getCourse(date) {
  const { data } = await apiClient.get(`/channels/courses/date/${date}`);
  return data.data;
}

export async function createCourse(channelId, param) {
  const { data } = await apiClient.post(`/channels/${channelId}/courses`, {
    startDate: param.startDate,
    endDate: param.endDate,
    location: param.location,
    statusType: param.statusType,
  });

  return data.data;
}

export async function updateCourse(channelId, courseId, param) {
  const { data } = await apiClient.patch(`/channels/${channelId}/courses/${courseId}`, {
    startDate: param.startDate,
    endDate: param.endDate,
    location: param.location,
    statusType: param.statusType,
  });
  return data.data;
}

export async function updateCourseStatus(channelId, courseId, param) {
  const { data } = await apiClient.patch(`/channels/${channelId}/courses/${courseId}`, {
    statusType: param.statusType,
  });
  return data.data;
}

export async function deleteCourse(channelId, courseId) {
  await apiClient.delete(`/channels/${channelId}/courses/${courseId}`);
}

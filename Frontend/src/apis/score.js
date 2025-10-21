import apiClient from '@/apis/apiClient';

export async function getGrades(channelId) {
  const { data } = await apiClient.get(`/channels/${channelId}/grades`);
  return data.data;
}

export async function createGrade(channelId, params) {
  const { data } = await apiClient.post(`/channels/${channelId}/grades`, params);
  return data.data;
}

export async function updateGrade(channelId, gradeId, params) {
  console.log('params', params);
  console.log('grade', params.grade);
  const { data } = await apiClient.put(`/channels/${channelId}/grades/${gradeId}`, params);
  return data.data;
}

export async function deleteGrade(channelId, gradeId) {
  const { data } = await apiClient.delete(`/channels/${channelId}/grades/${gradeId}`);
  return data.data;
}

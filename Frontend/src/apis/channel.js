import apiClient from '@/apis/apiClient';

// 채널 목록 조회
export async function getChannels(page = 0, size = 20) {
  const { data } = await apiClient.get('/channels', {
    params: { page, size, sort: 'createdAt,desc' },
  });
  return data.data; // ✅ 백엔드에서 data 배열 반환
}

export async function getChannel(channelId) {
  const { data } = await apiClient.get(`/channels/${channelId}`);
  return data.data[0];
}

// 채널 생성
export async function createChannel(payload) {
  const { data } = await apiClient.post('/channels', payload);
  return data.data; // ✅ 생성된 채널 데이터 배열
}

// 채널 수정
export async function updateChannel(channelId, payload) {
  const { data } = await apiClient.put(`/channels/${channelId}`, payload);
  return data.data;
}

// 채널 삭제
export async function deleteChannel(channelId) {
  const { data } = await apiClient.delete(`/channels/${channelId}`);
  return data.data;
}

//  초대 코드로 채널 참가
export async function joinChannel(code) {
  try {
    const response = await apiClient.post(`/channels/participants`, null, {
      params: { code },
    });
    return response.data.data;
  } catch (error) {
    console.error('채널 참가 실패:', error);
    throw error;
  }
}

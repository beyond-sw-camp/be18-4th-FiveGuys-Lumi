import apiClient from './apiClient';

export async function uploadFiles(type, files) {
  const formData = new FormData();
  formData.append('type', type); // 필요하다면 enum 값도 같이 보냄

  // 여러 파일을 files[] 배열로 append
  for (const file of files) {
    formData.append('file', file);
  }

  const res = await apiClient.post(`/${type}/files/upload`, formData, {
    headers: { 'Content-Type': 'multipart/form-data' },
  });

  return res.data.data; // [{fileId, fileName}, ...]
}

// 파일 삭제
export async function deleteFile(fileId) {
  const { data } = await apiClient.delete(`/files/${fileId}`);
  return data;
}

// 파일 다운로드
export async function downloadFile(fileId, fileName) {
  const res = await apiClient.get(`/files/${fileId}/download`, {
    responseType: 'blob',
  });

  // 브라우저에서 다운로드 트리거
  const blob = new Blob([res.data]);
  const url = window.URL.createObjectURL(blob);

  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download', fileName || `file-${fileId}`);
  document.body.append(link);
  link.click();
  link.remove();
  window.URL.revokeObjectURL(url);
}

import formatDateTime from '@/utils/formatDateTime';

export default function getCurrentMonthRange() {
  const now = new Date();
  const startDateTime = formatDateTime(new Date(now.getFullYear(), now.getMonth(), 1));
  const endDateTime = formatDateTime(new Date(now.getFullYear(), now.getMonth() + 1, 0));

  return { startDateTime, endDateTime };
}

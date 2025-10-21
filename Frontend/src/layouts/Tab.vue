<template>
  <v-tabs v-if="showTabs" v-model="tab" color="primary">
    <v-tab v-for="item in TABS" :key="item.to" replace :to="item.to">
      {{ item.label }}
    </v-tab>
  </v-tabs>

  <div class="pa-4 fill-height">
    <slot />
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { getChannel } from '@/apis/channel';
import { useApi } from '@/composable/useApi';

const route = useRoute();
const tab = ref(null);

const currentChannelId = computed(() => route.params.channelId);

const showTabs = computed(() => route.path.startsWith('/channels/'));

const { data: channel, queryFnExecute: useGetChannel } = useApi(getChannel);

onMounted(async () => {
  if (currentChannelId.value) {
    await useGetChannel(currentChannelId.value);
  }
});

const TABS = computed(() => {
  const channelId = currentChannelId.value;

  if (!channelId || !channel.value) return [];

  const baseTabs = [
    { label: '수업 과제', to: `/channels/${channelId}/assignments` },
    { label: '수업 자료', to: `/channels/${channelId}/materials` },
    { label: '성적', to: `/channels/${channelId}/scores` },
    { label: '참여자', to: `/channels/${channelId}/participants` },
  ];

  if (channel.value?.roleName === 'TUTOR') {
    baseTabs.unshift({ label: '수업 일정', to: `/channels/${channelId}/classes` });
  }

  return baseTabs;
});
</script>

<style scoped lang="scss"></style>

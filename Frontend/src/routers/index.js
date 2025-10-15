// @ts-nocheck
/**
 * router/index.js
 *
 * Automatic routes for `./src/pages/*.vue`
 */

import { createRouter, createWebHistory } from 'vue-router';
import { getChannel } from '@/apis/channel';
import Login from '@/pages/auth/Index.vue';
import { useAuthStore } from '@/stores/authStore';

const routes = [
  { path: '/', redirect: '/channels' },
  { path: '/login', component: Login, meta: { layout: 'blank' } },
  { path: '/channels', component: () => import('@/pages/channel/Index.vue'), meta: { layout: 'root' } },
  { path: '/calendar', component: () => import('@/pages/calendar/Index.vue'), meta: { layout: 'root' } },
  { path: '/chats', component: () => import('@/pages/chat/Index.vue'), meta: { layout: 'root' } },
  { path: '/profile', component: () => import('@/pages/setting/Index.vue'), meta: { layout: 'root' } },

  {
    path: '/channels/:channelId/classes',
    component: () => import('@/pages/class/Index.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
  {
    path: '/channels/:channelId/assignments',
    component: () => import('@/pages/assignment/Index.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
  {
    path: '/channels/:channelId/assignments/new',
    component: () => import('@/pages/assignment/AssignmentCreate.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
  {
    path: '/channels/:channelId/assignments/:assignmentId/edit',
    component: () => import('@/pages/assignment/AssignmentUpdate.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
  {
    path: '/channels/:channelId/assignments/:assignmentId',
    component: () => import('@/pages/assignment/AssignmentDetail.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
  {
    path: '/channels/:channelId/assignments/:assignmentId/submissions/:submissionId',
    component: () => import('@/pages/submission/Index.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
  {
    path: '/channels/:channelId/assignments/:assignmentId/submissions/new',
    component: () => import('@/pages/submission/SubmissionCreate.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
  {
    path: '/channels/:channelId/assignments/:assignmentId/submissions/:submissionId/edit',
    component: () => import('@/pages/submission/SubmissionUpdate.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
  {
    path: '/channels/:channelId/materials',
    component: () => import('@/pages/material/Index.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
  {
    path: '/channels/:channelId/materials/new',
    component: () => import('@/pages/material/MaterialCreate.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
  {
    path: '/channels/:channelId/materials/:materialId/edit',
    component: () => import('@/pages/material/MaterialUpdate.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
  {
    path: '/channels/:channelId/materials/:materialId',
    component: () => import('@/pages/material/MaterialDetail.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
  {
    path: '/channels/:channelId/scores',
    component: () => import('@/pages/score/Index.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
  {
    path: '/channels/:channelId/participants',
    component: () => import('@/pages/participant/Index.vue'),
    meta: { layout: 'root', subLayout: 'sublayout' },
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

router.onError((err, to) => {
  if (err?.message?.includes?.('Failed to fetch dynamically imported module')) {
    if (localStorage.getItem('vuetify:dynamic-reload')) {
      console.error('Dynamic import error, reloading page did not fix it', err);
    } else {
      console.log('Reloading page to fix dynamic import error');
      localStorage.setItem('vuetify:dynamic-reload', 'true');
      location.assign(to.fullPath);
    }
  } else {
    console.error(err);
  }
});

router.isReady().then(() => {
  localStorage.removeItem('vuetify:dynamic-reload');
});

router.beforeEach(async to => {
  const authStore = useAuthStore();

  try {
    if (!authStore.tokenInfo.accessToken) {
      await authStore.refreshAccessToken();
    }

    if (to.path === '/login' && authStore.tokenInfo.accessToken) {
      return { path: '/channels' };
    }

    if (to.name === undefined && to.path.includes('/channels') && to.path.includes('classes')) {
      const channelId = to.params.channelId;
      const channel = await getChannel(channelId);
      if (channel.roleName !== 'TUTOR') {
        return { path: `/channels/${channelId}/assignments` };
      }
    }
  } catch {
    if (to.path !== '/login') {
      return { path: '/login' };
    }
  }
});

export default router;

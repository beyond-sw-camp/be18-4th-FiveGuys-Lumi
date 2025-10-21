<template>
  <v-app class="bg-primary-lighten-1">
    <v-layout class="rounded rounded-md border">
      <component :is="layout">
        <component :is="subLayout">
          <router-view />
        </component>
      </component>
    </v-layout>
  </v-app>
</template>

<script>
import BlankLayout from '@/layouts/BlankLayout.vue';
import RootLayout from '@/layouts/RootLayout.vue';
import Tab from '@/layouts/Tab.vue';

export default {
  name: 'App',
  computed: {
    layout() {
      switch (this.$route.meta.layout) {
        case 'root': {
          return RootLayout;
        }
        case 'blank': {
          return BlankLayout;
        }
        default: {
          return BlankLayout;
        }
      }
    },
    subLayout() {
      if (this.$route.meta.subLayout === 'sublayout') return Tab;
      return BlankLayout;
    },
  },
};
</script>

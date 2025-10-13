import 'unfonts.css';

import App from './App.vue';
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import { registerPlugins } from '@/plugins';

const app = createApp(App);

registerPlugins(app);
app.use(createPinia());
app.use(createPinia());
app.mount('#app');

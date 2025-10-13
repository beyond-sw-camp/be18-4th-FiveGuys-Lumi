/**
 * plugins/index.js
 *
 * Automatically included in `./src/main.js`
 */

import router from '@/routers';
// Plugins
import vuetify from './vuetify';

export function registerPlugins(app) {
  app.use(vuetify).use(router);
}

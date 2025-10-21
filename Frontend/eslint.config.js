import eslintConfigPrettier from 'eslint-config-prettier/flat';
import vuetify from 'eslint-config-vuetify';

export default vuetify(
  {
    vue: true,
    ts: false,
  },
  {
    files: ['**/*.vue'],
    rules: {
      'vue/block-lang': 'off', // lang="ts" 강제 규칙 끄기
    },
  },
  {
    files: ['**/*.js', '**/*.vue'],
    ...eslintConfigPrettier,
  },
  {
    ignores: ['**/*.d.ts'],
  },
);

import Vue from 'vue';
import App from '@/App.vue';
import router from '@/router';
import store from '@/store';

import Paginate from 'vuejs-paginate';

import { BootstrapVue, IconsPlugin } from 'bootstrap-vue';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';

import VueWait from 'vue-wait';
import InfiniteScroll from 'vue-infinite-scroll';
import InfiniteLoading from 'vue-infinite-loading';
import 'codemirror/lib/codemirror.css';
import '@toast-ui/editor/dist/toastui-editor.css';
import '@toast-ui/editor/dist/toastui-editor-viewer.css';

import { Editor,Viewer } from '@toast-ui/vue-editor';

import Kakao from '@/initKakao.js';
Kakao.init();

import VueI18n from 'vue-i18n';
Vue.use(VueI18n);
import i18n from '@/i18n';

Vue.component('editor',Editor);
Vue.component('viewer',Viewer);
Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.use(VueWait);
Vue.use(InfiniteScroll);
Vue.use(InfiniteLoading);

Vue.component('paginate', Paginate);

import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import './assets/css/common.css';

Vue.config.productionTip = false;
import axios from '@/axios';
Vue.prototype.$axios = axios;

const vm = new Vue({
    i18n,
    router,
    store,
    wait: new VueWait(),
    render: (h) => h(App),
}).$mount('#app');

Vue.prototype.$toast = (title, message, append = false, hideDelay = 5000) => {
    vm.$bvToast.toast(`${message}`, {
        title: title,
        toaster: 'b-toaster-top-center',
        autoHideDelay: hideDelay,
        appendToast: append,
    });
};

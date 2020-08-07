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

import VueAwesomeSwiper from 'vue-awesome-swiper';
import { Swiper as SwiperClass, Pagination, Mousewheel, Autoplay } from 'swiper/js/swiper.esm';
import getAwesomeSwiper from 'vue-awesome-swiper/dist/exporter';
import 'swiper/css/swiper.css';

Vue.component('editor',Editor);
Vue.component('viewer',Viewer);
Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.use(VueWait);
Vue.use(InfiniteScroll);
Vue.use(InfiniteLoading);
Vue.use(VueAwesomeSwiper);
Vue.use(getAwesomeSwiper(SwiperClass));
SwiperClass.use([Pagination, Mousewheel, Autoplay]);

Vue.component('paginate', Paginate);

import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import './assets/css/common.css';



Vue.config.productionTip = false;
import axios from '@/axios';
Vue.prototype.$axios = axios;

const vm = new Vue({
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


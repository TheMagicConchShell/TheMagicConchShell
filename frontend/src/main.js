import Vue from 'vue';
import axios from 'axios';
import App from '@/App.vue';
import router from '@/router';
import store from '@/store';

import Paginate from 'vuejs-paginate';

import { BootstrapVue, IconsPlugin } from 'bootstrap-vue';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import VueCarousel from 'vue-carousel';
import VueWait from 'vue-wait';
import InfiniteLoading from 'vue-infinite-loading';

Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.use(VueWait);
Vue.use(InfiniteLoading);
Vue.use(VueCarousel);

Vue.component('paginate', Paginate);

import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
import './assets/css/common.css';

Vue.config.productionTip = false;

Vue.prototype.$axios = axios.create({
    // baseURL: 'http://localhost:8080'
    baseURL: 'http://i3a403.p.ssafy.io:8399',
});

Vue.prototype.$axios.interceptors.response.use(
    (response) => response,
    (error) => {
        if (!error.response) {
            // Server do not response
            // vm.$router.push({
            //     name: 'ERROR',
            //     params: { error: 'error' },
            // });
        }

        const message = error.response.data.message;
        const vm = new Vue();
        vm.$bvToast.toast(`${message}`, {
            title: `Error ${error.response.status} (${error.response.data.status})`,
            toaster: 'b-toaster-top-center',
            autoHideDelay: 5000,
        });

        return Promise.reject(error);
    },
);

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
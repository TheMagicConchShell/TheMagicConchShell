import axios from 'axios';
import store from '@/store';

import Vue from 'vue';
import { BootstrapVue } from 'bootstrap-vue';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-vue/dist/bootstrap-vue.css';
Vue.use(BootstrapVue);

const instance = axios.create({
    // baseURL: 'http://localhost:8399'
    baseURL: 'http://i3a403.p.ssafy.io:8399',
});

instance.interceptors.request.use(
    (config) => {
        if (store.getters.nickname) {
            config.headers['jwt-auth-token'] = store.getters.jwtAuthToken;
            config.headers['nickname'] = store.getters.encodedNickname;
        }
        
        return config;
    }
);

instance.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        if (!error.response) {
            // Server do not response
            // vm.$router.push({
            //     name: 'ERROR',
            //     params: { error: 'error' },
            // });
        }

        console.log(error);
        // const message = error.response.data.message;
        // const vm = new Vue();
        // vm.$bvToast.toast(`${message}`, {
        //     title: `Error ${error.response.status} (${error.response.data.status})`,
        //     toaster: 'b-toaster-top-center',
        //     autoHideDelay: 5000,
        // });

        return Promise.reject(error);
    },
);

export default instance;
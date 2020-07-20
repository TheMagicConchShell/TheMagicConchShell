import Vue from 'vue';
import axios from 'axios';
import App from './App.vue';
import router from './router';
import store from './store';

Vue.config.productionTip = false;

const vm = new Vue({
    router,
    store,
    render: (h) => h(App),
}).$mount('#app');

axios.interceptors.response.use(
    (response) => response,
    (error) => {
        if (!error.response) {
            // Server do not response
            vm.$router.push({
                name: 'ERROR',
                params: { error: 'error' },
            });
        }

        return Promise.reject(error);
    },
);

Vue.prototype.$axios = axios;

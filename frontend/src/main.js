import Vue from 'vue';
import axios from 'axios';
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
import 'tui-color-picker/dist/tui-color-picker.css';
import colorSyntax from '@toast-ui/editor-plugin-color-syntax';
import { Editor,Viewer } from '@toast-ui/vue-editor';

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

Vue.prototype.$axios = axios.create({
    // baseURL: 'http://localhost:8399'
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

Vue.prototype.$editorOptions = {
    hooks:{
        addImageBlobHook :  async (blob, callback)=>{
            var u = window.URL.createObjectURL(blob);
            var img = new Image();
            img.src = u;
            var canvas = document.createElement('canvas');
            img.onload=function(){
                var MAX_WIDTH = 1000;
                var MAX_HEIGHT = 800;
                var width = img.width;
                var height = img.height;
                    
                if (width > height) {
                    if (width > MAX_WIDTH) {
                        height *= MAX_WIDTH / width;
                        width = MAX_WIDTH;
                    }
                } else {
                    if (height > MAX_HEIGHT) {
                        width *= MAX_HEIGHT / height;
                        height = MAX_HEIGHT;
                    }
                }
                canvas.width = width;
                canvas.height = height;
                var ctx = canvas.getContext('2d');
                ctx.drawImage(img,0,0,width,height);
                var ret = canvas.toDataURL();
                callback(ret,"uploaded image");
            };
            
            return false;
        }
    },
    plugins: [colorSyntax],
};

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


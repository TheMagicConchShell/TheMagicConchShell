import Vue from 'vue';
import Vuex from 'vuex';
import axios from 'axios';

Vue.use(Vuex);

const Axios = axios.create({
    baseURL: 'http://i3a403.p.ssafy.io:8399',
});

Axios.interceptors.response.use(
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

export const state = {
    nickname: '',
    jwtAuthToken: '',
};

export const mutations = {
    LOGIN_SUCCESS_TOKEN(state, jwtAuthToken){
        state.jwtAuthToken = jwtAuthToken;
    },
    LOGIN_SUCCESS_NICKNAME(state, nickname){
        state.nickname = nickname;
    },
    LOGOUT(state){
        state.nickname = '';
        state.jwtAuthToken = '';
    },
};

export const getters = {
    nickname: (state) => {
        return state.nickname;
    },
};

export const actions = {
    login({commit, dispatch}, {email, password}) {
        return new Promise((resolve, reject) => {
            commit('LOGOUT');
            Axios({
                method: "post",
                url: "/user/login",
                data: {
                    email,
                    password,
                },
            })
                .then((res) => {
                    const jwtAuthToken = res.headers['jwt-auth-token'];
                    const nickname = res.headers['nickname'];
                    
                    axios.defaults.headers.common['nickname'] = nickname;
                    axios.defaults.headers.common['jwt-auth-token'] = jwtAuthToken;

                    dispatch("loginSuccess", { jwtAuthToken, nickname });
                    resolve(res);
                })
                .catch((error) => {
                    console.log(error.response);
                    reject(error);
                });
        });
    },
    loginSuccess({ commit }, { jwtAuthToken, nickname }){
        commit('LOGIN_SUCCESS_TOKEN', jwtAuthToken);
        commit('LOGIN_SUCCESS_NICKNAME', nickname);
    },
    logout({ commit }){
        return new Promise((resolve) => {
            commit('LOGOUT');

            delete axios.defaults.headers.common['nickname'];
            delete axios.defaults.headers.common['jwt-auth-token'];

            resolve();
        });
    }
};

export const modules = {
};
import Vue from 'vue';
import Vuex from 'vuex';
import axios from '@/axios';

Vue.use(Vuex);

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
        state.nickname = null;
        state.jwtAuthToken = null;
    },
};

export const getters = {
    nickname: (state) => {
        return state.nickname;
    },
    jwtAuthToken: (state) => {
        return state.jwtAuthToken;
    },
};

export const actions = {
    login({commit, dispatch}, {email, password}) {
        return new Promise((resolve, reject) => {
            commit('LOGOUT');
            axios({
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

                    commit('LOGIN_SUCCESS_TOKEN', jwtAuthToken);
                    commit('LOGIN_SUCCESS_NICKNAME', nickname);
                    resolve(res);
                })
                .catch((error) => {
                    console.log(error.response);
                    reject(error);
                });
        });
    },
    logout({ commit }){
        return new Promise((resolve) => {
            commit('LOGOUT');

            resolve();
        });
    },
    register({  }, { email, nickname, password }) {
        return new Promise((resolve, reject) => {
            axios({
                method: 'post',
                url: '/user/signup',
                data: {
                    email,
                    nickname,
                    password,
                },
            })
                .then((res) => {
                    resolve(res);
                })
                .catch((error) => {
                    console.log(error.response);
                    reject(error);
                });
        });
    }
};

export const modules = {
};
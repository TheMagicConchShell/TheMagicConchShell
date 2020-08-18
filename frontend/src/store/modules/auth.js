import Vue from 'vue';
import Vuex from 'vuex';
import axios from '@/axios';

import { sha256 } from '@/util/hash.js';

Vue.use(Vuex);

export const state = {
    nickname: '',
    jwtAuthToken: '',
};

export const mutations = {
    LOGIN_SUCCESS(state, { jwtAuthToken, nickname }){
        state.jwtAuthToken = jwtAuthToken;
        state.nickname = decodeURI(nickname);
    },
    TOKEN_UPDATE_SUCCESS(state, { jwtAuthToken, nickname }){
        state.jwtAuthToken = jwtAuthToken;
        state.nickname = decodeURI(nickname);
    },
    LOGOUT(state){
        state.nickname = null;
        state.jwtAuthToken = null;
    },
};

export const getters = {
    encodedNickname: (state) => {
        return encodeURI(state.nickname);
    },
    nickname: (state) => {
        return state.nickname;
    },
    jwtAuthToken: (state) => {
        return state.jwtAuthToken;
    },
};

export const actions = {
    login({ commit }, { email, password }) {
        return new Promise((resolve, reject) => {
            commit('LOGOUT');
            
            axios({
                method: 'post',
                url: '/user/login',
                data: {
                    email,
                    password: sha256(password),
                },
            })
                .then((response) => {
                    if (200 <= response.status && response.status < 300) {
                        const jwtAuthToken = response.headers['jwt-auth-token'];
                        const nickname = response.headers['nickname'];
    
                        commit('LOGIN_SUCCESS', { jwtAuthToken, nickname });

                        resolve(response);
                    } else {
                        reject(response);
                    }
                })
                .catch((error) => {
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
                    password: sha256(password),
                },
            })
                .then((response) => {
                    if (200 <= response.status && response.status < 300) {
                        resolve(response);
                    } else {
                        reject(response);
                    }
                })
                .catch((error) => {
                    reject(error);
                });
        });
    },
    authentication({ }, { aid, token }) {
        return new Promise((resolve, reject) => {
            axios({
                method: 'get',
                url: '/user/authentication',
                params: {
                    aid,
                    token,
                }
            })
                .then((response) => {
                    if (200 <= response.status && response.status < 300) {
                        resolve(response);
                    } else {
                        reject(response);
                    }
                })
                .catch((error) => {
                    reject(error);
                });
        });
    },
    fetchUser({ }, { nickname }) {
        return new Promise((resolve, reject) => {
            axios({
                method: 'get',
                url: '/user/detail',
                params: {
                    nickname,
                }
            })
                .then((response) => {
                    if (200 <= response.status && response.status < 300) {
                        resolve(response);
                    } else {
                        reject(response);
                    }
                })
                .catch((error) => {
                    reject(error);
                });
        });
    },
    deleteUser({ commit }, { nickname }) {
        return new Promise((resolve, reject) => {
            axios({
                method: 'delete',
                url: '/user/delete',
                params: {
                    nickname,
                }
            })
                .then((response) => {
                    if (200 <= response.status && response.status < 300) {
                        commit('LOGOUT');

                        resolve(response);
                    } else {
                        reject(response);
                    }
                })
                .catch((error) => {
                    reject(error);
                });
        });
    },
    updateUser({ commit }, formData) {
        formData.set('password', sha256(formData.get('password')));
        return new Promise((resolve, reject) => {
            axios({
                method: 'put',
                url: '/user/update',
                data: formData,
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            })
                .then((response) => {
                    if (200 <= response.status && response.status < 300) {
                        const jwtAuthToken = response.headers['jwt-auth-token'];
                        const nickname = response.headers['nickname'];
    
                        commit('TOKEN_UPDATE_SUCCESS', { jwtAuthToken, nickname });

                        resolve(response);
                    } else {
                        reject(response);
                    }
                })
                .catch((error) => {
                    reject(error);
                });
        });
    },
    findPassword({ }, { email, nickname }) {
        return new Promise((resolve, reject) => {
            axios({
                method: 'post',
                url: '/user/findpw',
                params: {
                    email,
                    nickname,
                }
            })
                .then((response) => {
                    if (200 <= response.status && response.status < 300) {
                        resolve(response);
                    } else {
                        reject(response);
                    }
                })
                .catch((error) => {
                    reject(error);
                });
        });
    },
    extendJWT() {
        return new Promise((resolve, reject) => {
            axios({
                method: 'get',
                url: '/user/extendJWT',
            })
                .then((response) => {
                    if (200 <= response.status && response.status < 300) {
                        const jwtAuthToken = response.headers['jwt-auth-token'];
                        const nickname = response.headers['nickname'];
    
                        commit('TOKEN_UPDATE_SUCCESS', { jwtAuthToken, nickname });

                        resolve(response);
                    } else {
                        commit('LOGOUT');
                        
                        reject(response);
                    }
                })
                .catch((error) => {
                    commit('LOGOUT');

                    reject(error);
                });
        });
    },
};

export const modules = {
};
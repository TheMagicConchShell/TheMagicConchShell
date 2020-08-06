import Vue from 'vue';
import Vuex from 'vuex';
import axios from '@/axios';

Vue.use(Vuex);

export const state = {
    nickname: '',
    jwtAuthToken: '',
};

export const mutations = {
    LOGIN_SUCCESS(state, { jwtAuthToken, nickname }){
        state.jwtAuthToken = jwtAuthToken;
        state.nickname = nickname;
    },
    TOKEN_UPDATE_SUCCESS(state, { jwtAuthToken, nickname }){
        state.jwtAuthToken = jwtAuthToken;
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
    login({ commit }, { email, password }) {
        return new Promise((resolve, reject) => {
            commit('LOGOUT');
            
            axios({
                method: 'post',
                url: '/user/login',
                data: {
                    email,
                    password,
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
                    password,
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
            commit('LOGOUT');

            axios({
                method: 'delete',
                url: '/user/delete',
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
    updateUser({ commit }, { email, nickname, password, profileImg }) {
        return new Promise((resolve, reject) => {
            axios({
                method: 'put',
                url: '/user/update',
                data: {
                    email: email,
                    nickname: nickname,
                    password: password,
                    profileImg: profileImg,
                },
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
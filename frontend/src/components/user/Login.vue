<template>
    <div>
        <h1>로그인</h1>

        <div>
            <input
                id="email"
                ref="email"
                v-model="email"
                type="text"
                placeholder="아이디"
            >
        </div>

        <div>
            <input
                id="password"
                ref="password"
                v-model="password"
                type="password"
                placeholder="비밀번호"
            >
        </div>

        <div>
            <button @click="login">
                로그인
            </button>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

const storage = window.sessionStorage;

export default {
    data: () => ({
        email: '',
        password: '',
    }),
    methods: {
        login() {
            // empty check
            if (!this.email && this.password) {
                // 입력 요청
            }

            storage.setItem('jwt-auth-token', '');
            storage.setItem('login_user', '');
            axios({
                method: 'post',
                url: 'http://localhost:8080/api/user/login',
                data: {
                    email: this.email,
                    password: this.password,
                },
            }).then((res) => {
                if (res.data.status) {
                    // 로그인 성공
                    console.log('login success');
                    storage.setItem('jwt-auth-token', res.headers['jwt-auth-token']);
                    storage.setItem('login_user', res.data.object.uid);
                } else {
                    // 로그인 실패
                    console.log('login fail');
                }
            }).catch(() => {
                // 로그인 실패
            });
        },
    },
};
</script>

<style scoped>
</style>

<template>
    <div>
        <h1>비밀번호 찾기</h1>

        <div>
            <label for="email">이메일</label>
            <input
                id="email"
                ref="email"
                v-model="email"
                type="text"
                placeholder="아이디"
            >
        </div>

        <div>
            <label for="nickname">닉네임</label>
            <input
                id="nickname"
                ref="nickname"
                v-model="nickname"
                type="text"
                placeholder="닉네임"
            >
        </div>

        <div>
            <button @click="findPassword">
                비밀번호 찾기 (이메일 전송)
            </button>
        </div>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data: () => ({
        email: '',
        nickname: '',
        msg: '',
    }),
    methods: {
        findPassword() {
            // empty check
            if (!this.email && !this.nickname) {
                this.msg = '아이디(이메일)와 닉네임을 입력해주세요.';
                this.makeToast();
                return;
            }

            axios({
                method: 'post',
                url: 'http://localhost:8080/user/findpw',
                data: {
                    email: this.email,
                    nickname: this.nickname,
                },
            }).then((res) => {
                if (res.data.status === 'S-200') {
                    // 이메일 전송 성공
                    this.msg = '비밀번호가 이메일로 전송되었습니다. 발송된 메일의 비밀번호로 로그인하여 주세요.';
                    this.makeToast();
                }
            }).catch((error) => {
                if (error.response.data.status === 'E-4002') {
                    // 없는 이메일
                    this.msg = error.response.data.errors.message;
                    this.makeToast();
                } else if (error.response.data.status === 'E-4004') {
                    // 닉네임 불일치
                    this.msg = error.response.data.errors.message;
                    this.makeToast();
                } else if (error.response.data.status === 'E-4007') {
                    // 메일 발송 실패
                    this.msg = error.response.data.errors.message;
                    this.makeToast();
                }
            });
        },
        makeToast(append = false) {
            this.$bvToast.toast(`${this.msg}`, {
                title: 'Notice',
                toaster: 'b-toaster-top-center',
                autoHideDelay: 5000,
                appendToast: append,
            });
        },
    },
};
</script>

<style scoped>
</style>

<template>
    <div>
        <h1>로그인</h1>

        <div>
            <label for="email">아이디</label>
            <input
                id="email"
                ref="email"
                v-model="email"
                type="text"
                placeholder="아이디"
            >
        </div>

        <div>
            <label for="password">비밀번호</label>
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
const storage = window.sessionStorage;

export default {
    data: () => ({
        email: '',
        password: '',
        msg: '',
    }),
    methods: {
        login() {
            // empty check
            if (!this.email || !this.password) {
                this.msg = '아이디(이메일)와 비밀번호를 입력해주세요.';
                this.makeToast();
                return;
            }

            storage.setItem('jwt-auth-token', '');
            storage.setItem('login_user', '');
            this.$axios({
                method: 'post',
                url: '/user/login',
                data: {
                    email: this.email,
                    password: this.password,
                },
            }).then((res) => {
                console.log(res);
                storage.setItem('jwt-auth-token', res.headers['jwt-auth-token']);
                storage.setItem('login_user', res.data.data.uid);
            }).catch((error) => {
                console.log(error.response);
            });
        },
        makeToast(append = false) {
            this.$bvToast.toast(`${this.msg}`, {
                title: 'Notice',
                toaster: 'b-toaster-top-center',
                autoHideDelay: 5000,
                appendToast: append,
            });
        }
    },
};
</script>

<style scoped>
</style>
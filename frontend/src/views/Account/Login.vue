<template>
    <div>
        Log in
        <b-modal
            id="login"
            title="로그인"
            hide-footer
        >
            <div class="d-flex justify-content-center">
                <img
                    id="photo"
                    src="../../assets/images/conch_shell.jpeg"
                >
            </div>
            <input
                id="email"
                ref="email"
                v-model="email"
                type="text"
                placeholder="아이디"
            >
            <input
                id="password"
                ref="password"
                v-model="password"
                type="password"
                placeholder="비밀번호"
            >
            <div>
                <b-button 
                    v-b-modal.signup
                    variant="link"
                >
                    아직 회원이 아니신가요?
                </b-button>
            </div>
            <div>
                <b-button 
                    v-b-modal.findpw
                    variant="link"
                >
                    비밀번호를 잊으셨나요?
                    <findPW />
                </b-button>
            </div>
            <div class="float-right">
                <b-button
                    id="commonbutton"
                    @click.prevent="login"
                >
                    로그인
                </b-button>
                <b-button
                    ref="loginCloseBtn"
                    @click="$bvModal.hide('login')"
                >
                    닫기
                </b-button>
            </div>
        </b-modal>
    </div>
</template>

<script>
import Signup from './Signup.vue';
import FindPW from './FindPassword.vue';

const storage = window.sessionStorage;

export default {
    components: {
        FindPW,
    },
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
                this.$bvToast.toast(this.msg);
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
                this.onClickLoginSuccess();
                this.$router.go();
            }).catch((error) => {
                console.log(error.response);
            });
        },
        onClickLoginSuccess() {
            this.$refs.loginCloseBtn.click();
        },
    },
};
</script>

<style scoped>
</style>

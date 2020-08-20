<template>
    <div>
        {{ $t('layout.login') }}
        <b-modal
            id="login"
            hide-header
            hide-footer
        >
            <div class="d-flex justify-content-center">
                <img
                    id="photo"
                    src="../../assets/images/circle-sora.png"
                >
            </div>
            <div
                class="d-flex"
                style="align-items:center;"
            >
                <i class="far fa-envelope" />
                <b-input
                    id="email"
                    ref="email"
                    v-model="email"
                    type="text"
                    placeholder="이메일"
                />
            </div>
            <div
                class="d-flex"
                style="align-items:center;"
            >
                <i class="fas fa-lock" />
                <b-input
                    id="password"
                    ref="password"
                    v-model="password"
                    type="password"
                    placeholder="비밀번호"
                />
            </div>
            <div>
                <b-button
                    block
                    pill
                    variant="outline-primary"
                    class="login-btn"
                    @click.prevent="login"
                >
                    로그인
                </b-button>
            </div>
            <div class="help-btn">
                <b-button
                    v-b-modal.signup
                    variant="link"
                >
                    아직 회원이 아니신가요?
                </b-button>
            </div>
            <div class="help-btn">
                <b-button
                    v-b-modal.findpw
                    variant="link"
                >
                    비밀번호를 잊으셨나요?
                    <findPW />
                </b-button>
            </div>
            <hr>
            <div class="help-btn">
                <KakaoLogin />
            </div>
        </b-modal>
    </div>
</template>

<script>
import FindPW from "@/components/account/FindPassword.vue";
import KakaoLogin from '@/components/account/KakaoLogin.vue';

export default {
    components: {
        FindPW,
        KakaoLogin
    },
    data: () => ({
        email: "",
        password: "",
        msg: "",
    }),
    methods: {
        login() {
            // empty check
            if (!this.email || !this.password) {
                this.msg = "아이디(이메일)와 비밀번호를 입력해주세요.";
                this.$toast("안내", this.msg);
                return;
            }

            this.$store.dispatch("login", {
                email: this.email, 
                password: this.password
            })
                .then((res) => {
                    this.$toast('안내', '로그인 되었습니다.');
                })
                .catch((err) => {
                    if(err.response.status==400){
                        this.$toast('로그인 실패', err.response.data.message);
                    }else if(err.response.status==404){
                        this.$toast('로그인 실패', err.response.data.message);
                    }else {
                        this.$toast('로그인 실패','로그인에 실패했습니다.');
                    }
                });
        },
    },
};
</script>

<style scoped>
i {
    flex: 1 1 30px;
}
img {
    max-width: 100px;
    max-height: 100px;
    margin: 1rem;
}
.login-btn {
    margin-top: 1rem;
    margin-bottom: 1rem;
}
.help-btn {
    text-align: center;
}
</style>

<template>
    <div>
        <b-modal
            id="findpw"
            hide-header
            hide-footer
        >
            <div
                id="find-pw-header"
                class="center"
            >
                <i 
                    class="fas fa-unlock-alt"
                    style="margin-right: 0.5rem;"
                />
                비밀번호 찾기
            </div>
            <div
                id="find-pw-text"
                class="center"
            >
                가입한 이메일과 별명을 입력해주세요.
            </div>
            <div class="d-flex input">
                <i class="far fa-envelope" />
                <b-input
                    id="email"
                    ref="email"
                    v-model="email"
                    type="text"
                    placeholder="이메일"
                />
            </div>

            <div class="d-flex input">
                <i class="fas fa-user-circle" />
                <b-input
                    id="nickname"
                    ref="nickname"
                    v-model="nickname"
                    type="text"
                    placeholder="별명"
                />
            </div>

            <b-button
                id="find-pw-btn"
                block
                pill
                variant="outline-primary"
                @click="findPassword"
            >
                비밀번호 찾기
            </b-button>
            <div class="center">
                <b-button
                    pill
                    variant="link"
                    @click="$bvModal.hide('findpw')"
                >
                    <i class="fas fa-times" />
                </b-button>               
            </div>
        </b-modal>
    </div>
</template>

<script>
export default {
    data: () => ({
        email: '',
        nickname: '',
        msg: '',
    }),
    methods: {
        findPassword() {
            // empty check
            if (!this.email || !this.nickname) {
                this.msg = '아이디(이메일)와 닉네임을 입력해주세요.';
                this.$toast('안내', this.msg);
                return;
            }

            this.$store.dispatch('findPassword', {
                email: this.email,
                nickname: this.nickname,
            })
                .then((response) => {
                    // 이메일 전송 성공
                    this.msg = '메일로 임시 비밀번호가 발급되었습니다!';
                    this.$toast('안내', this.msg);
                    this.$bvModal.hide('findpw');
                })
                .catch((error) => {
                    console.log(error.response);
                });
        },
    },
};
</script>

<style scoped>
i {
    flex: 1 1 30px;
}
.input {
    align-items: center;
}
#find-pw-btn {
    margin: 1rem 0 0.5rem 0;
}
#find-pw-header {
    font-size: 1.2rem;
    margin: 1rem 0 1rem 0;
}
#find-pw-text {
    font-size: 1.1rem;
    margin-bottom: 1rem;
}
.center {
    text-align: center;
}
</style>

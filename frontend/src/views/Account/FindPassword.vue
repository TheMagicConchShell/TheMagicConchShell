<template>
    <div>
        <b-modal
            id="findpw"
            title="비밀번호 찾기"
            hide-footer
        >
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
                    id="nickname"
                    ref="nickname"
                    v-model="nickname"
                    type="text"
                    placeholder="닉네임"
                >
            </div>

            <div>
                <b-button @click="findPassword">
                    비밀번호 찾기 (이메일 전송)
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
                this.$bvToast.toast(this.msg);
                return;
            }

            this.$axios({
                method: 'post',
                url: `/user/findpw?email=${this.email}&nickname=${this.nickname}`,
            }).then((res) => {
                if (res.data.status === 'S-200') {
                    // 이메일 전송 성공
                    this.msg = '비밀번호가 이메일로 전송되었습니다. 발송된 메일의 비밀번호로 로그인하여 주세요.';
                    this.$bvToast.toast(this.msg);
                }
            }).catch((error) => {
                console.log(error.response);
            });
        },
    },
};
</script>

<style scoped>
</style>

<template>
    <div>
        <div>
            <div>
                <h1>이메일 인증</h1>
                <h3 id="authText">
                    {{ text }}
                </h3>

                <button @click="moveMain">
                    <span>
                        메인화면으로 이동
                    </span>
                </button>
            </div>
        </div>
    </div>
</template>

<script>
// import axios from 'axios';

export default {
    name: 'EmailAuthentication',
    data() {
        return {
            text: '',
            aid: '',
            token: '',
        };
    },
    created() {
        this.aid = this.$route.query.aid;
        this.token = this.$route.query.token;
        console.log(this.aid);
        console.log(this.token);
        this.$axios({
            method: 'get',
            url: `/user/authentication?aid=${this.aid}&token=${this.token}`,

        }).then((res) => {
            if (res.data.status === 'S-200') {
                this.text = `이메일 인증이 완료되었습니다.
                 정상적으로 서비스 이용이 가능합니다.`;
            } else {
                this.text = '이메일 인증에 실패하였습니다. 다시 시도해주세요.';
            }
        }).catch((error) => {
            this.text = '이메일 인증에 실패하였습니다. 다시 시도해주세요.';
            console.log(error.response);
        });
    },
    methods: {
        moveMain() {
            this.$router.push('/');
        },
    },
};
</script>

<style>
#authText{
    text-align: center;
}
</style>

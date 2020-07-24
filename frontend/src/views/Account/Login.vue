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
                type="text"
                placeholder="아이디"
            >
            <input
                type="password"
                placeholder="비밀번호"
            >
            <div class="float-right">
                <b-button id="commonbutton">
                    로그인
                </b-button>
            </div>
        </b-modal>
    </div>
</template>

<script>
export default {

};
</script>

<style scoped>
</style>

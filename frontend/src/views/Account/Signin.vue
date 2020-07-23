<template>
    <div>
        Sign in
        <b-modal
            id="signin"
            title="회원 가입"
            hide-footer
        >
            <div class="row">
                <div class="col-5">
                    <div id="photo" />
                </div>
                <div class="col-7">
                    <input
                        type="text"
                        placeholder="이름"
                    >
                    <input
                        type="text"
                        placeholder="아이디"
                    >
                    <input
                        type="password"
                        placeholder="비밀번호"
                    >
                    <input
                        type="password"
                        placeholder="비밀번호 확인"
                    >
                    <input
                        type="text"
                        placeholder="닉네임"
                    >
                    <textarea
                        cols="30"
                        rows="5"
                        placeholder="자기 소개"
                    />
                </div>
            </div>
            <button> 회원 가입</button>
        </b-modal>
    </div>
</template>

<script>
export default {

};
</script>

<style scoped>
#join {
    margin: 10px;
}
</style>

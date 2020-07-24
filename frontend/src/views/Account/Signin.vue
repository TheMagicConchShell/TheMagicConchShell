<template>
    <div>
        Sign in
        <b-modal
            id="signin"
            title="회원 가입"
        >
            <div class="row">
                <div class="col-5">
                    <img
                        v-if="photoUrl"
                        id="profilephoto"
                        :src="photoUrl"
                        alt="사진"
                    >
                    <img
                        v-else
                        id="profilephoto"
                        src="../../assets/images/abstract.jpg"
                    >
                    <input
                        type="file"
                        accept="image/*"
                        @change="onChange"
                    >
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
                </div>
            </div>
            <div class="float-right">
                <b-button
                    id="commonbutton"
                >
                    회원 가입
                </b-button>
            </div>
            <template
                v-slot:modal-footer
            >
                <b-button
                    id="button"
                    size="sm"
                    style="background-color: #FFEB3B; color:#3E2723;"
                >
                    <img
                        src="../../assets/images/kakaotalk.png"
                        style="max-height:100%;"
                    >
                    카톡으로 가입
                </b-button>
                <b-button
                    id="button"
                    size="sm"
                    variant="secondary"
                    style="background-color: #6B799E;"
                >
                    <img
                        src="../../assets/images/github.png"
                        style="max-height:100%;"
                    >
                    깃헙으로 가입
                </b-button>
            </template>
        </b-modal>
    </div>
</template>

<script>
export default {
    name: 'Signin',
    data() {
        return {
            photo: null,
            photoUrl: null,
        };
    },
    methods: {
        onChange(e) {
            const file = e.target.files[0];
            this.photo = file;
            this.photoUrl = URL.createObjectURL(file);
        },
    },

};
</script>

<style scoped>
#button {
    height: 100%;
    border: 0;
}
</style>

<template>
    <div>
        Profile
        <b-modal
            id="userdetail"
            title="회원 정보"
            hide-footer
        >
            <ValidationObserver ref="observer">
                <div>
                    프로필 사진
                    <b-avatar
                        variant="info"
                        :src="imageUrl"
                        size="6em"
                    />
                    <input
                        ref="imageInput"
                        type="file"
                        accept="image/*"
                        hidden
                        @change="onChangeImages"
                    >
                    <button
                        class="fas fa-camera"
                        type="button"
                        @click="onClickImageUpload"
                    />
                </div>

                <div>
                    <label for="email">이메일</label>
                    <input
                        id="email"
                        ref="email"
                        v-model="email"
                        type="text"
                        placeholder="ssafy@example.com"
                        readonly
                    >
                </div>

                <div>
                    <label for="nickname">별명</label>
                    <ValidationProvider
                        v-slot="{ errors }"
                        class="input-wrap"
                        :rules="{required: true, max: 25}"
                    >
                        <input
                            id="nickname"
                            ref="nickname"
                            v-model="nickname"
                            type="text"
                            placeholder="별명"
                        >
                        <div
                            v-if="errors[0]"
                            class="validation-fail-message"
                        >
                            {{ errors[0] }}
                        </div>
                    </validationprovider>
                </div>

                <div>
                    <label for="password">비밀번호</label>
                    <ValidationProvider
                        v-slot="{ errors }"
                        class="input-wrap password-wrap"
                        :rules="{
                            required: true,
                            min: 8,
                            passwordRegex: true,
                            passwordConfirm: passwordConfirm }"
                    >
                        <input
                            id="password"
                            ref="password"
                            v-model="password"
                            :type="passwordType"
                            placeholder="영문/숫자 포함 8자 이상"
                        >
                        <i
                            :class="{ 'fa fa-eye fa-lg' : passwordType === 'password' ,
                                      'fa fa-eye-slash fa-lg' : passwordType === 'text' }"
                            @click="passwordType === 'text'?
                                passwordType = 'password': passwordType = 'text'"
                        />
                        <div
                            v-if="errors[0]"
                            class="validation-fail-message"
                        >
                            {{ errors[0] }}
                        </div>
                    </validationprovider>
                </div>

                <div>
                    <label for="passwordConfirm">비밀번호 확인</label>
                    <ValidationProvider
                        v-slot="{ errors }"
                        class="input-wrap password-wrap"
                        vid="password-confirm"
                        rules="required"
                    >
                        <input
                            id="passwordConfirm"
                            ref="passwordConfirm"
                            v-model="passwordConfirm"
                            :type="passwordType"
                            placeholder="비밀번호 확인"
                        >
                        <div
                            v-if="errors[0]"
                            class="validation-fail-message"
                        >
                            {{ errors[0] }}
                        </div>
                    </validationprovider>
                </div>

                <div>
                    <label for="point">포인트</label>
                    <input
                        id="point"
                        ref="point"
                        v-model="point"
                        type="text"
                        readonly
                    >
                </div>

                <div>
                    <label for="level">레벨</label>
                    <input
                        id="level"
                        ref="level"
                        v-model="level"
                        type="text"
                        readonly
                    >
                </div>

                <div>
                    <b-button @click="userUpdate">
                        정보 수정
                    </b-button>
                    <b-button @click="userDelete">
                        회원 탈퇴
                    </b-button>
                </div>
            </ValidationObserver>
        </b-modal>
    </div>
</template>

<script>
import {
    ValidationObserver,
    ValidationProvider,
    extend,
} from 'vee-validate';
import {
    min, max, required,
} from 'vee-validate/dist/rules';

extend('passwordConfirm', {
    params: ['target'],
    validate(value, { target }) {
        return value === target;
    },
    message: '비밀번호가 일치하지 않습니다.',
});

extend('required', {
    ...required,
    message: '반드시 입력해야하는 항목입니다.',
});

extend('min', {
    ...min,
    message: '{length}글자 이상으로 입력해주세요.',
});

extend('max', {
    ...max,
    message: '{length}글자 이하로 입력해주세요.',
});

extend('passwordRegex', {
    validate(value) {
        return /^(?=.*?[A-Z])|(?=.*?[a-z])(?=.*?[0-9]).*$/.test(value);
    },
    message: '알파벳과 숫자를 각각 1개 이상 포함해야합니다.',
});

const storage = window.sessionStorage;

export default {
    components: {
        ValidationProvider,
        ValidationObserver,
    },
    data: () => ({
        uid: '',
        email: '',
        nickname: '',
        password: '',
        passwordConfirm: '',
        profileImg: '',
        point: '',
        level: '',
        passwordType: 'password',
        imageUrl: '',
        msg: '',
    }),
    created() {
        this.uid = storage.getItem('login_user');
        this.$axios({
            method: 'get',
            url: `/user/detail?uid=${this.uid}`,
        }).then((res) => {
            if (res.data.status === 'S-200') {
                // 정보 조회 성공
                this.email = res.data.data.email;
                this.nickname = res.data.data.nickname;
                this.profileImg = res.data.data.profileImg;
                this.point = res.data.data.point;
                this.level = res.data.data.level;
            }
        }).catch((error) => {
            console.log(error.response);
        });
    },
    methods: {
        async userUpdate() {
            const isValid = await this.$refs.observer.validate();

            if (!isValid) {
                this.msg = '필수 항목을 모두 입력해주세요.';
                this.$bvToast.toast(this.msg);
                return;
            }

            this.$axios({
                method: 'put',
                url: '/user/update',
                data: {
                    uid: this.uid,
                    email: this.email,
                    nickname: this.nickname,
                    password: this.password,
                    profileImg: this.profileImg,
                },
            }).then((res) => {
                if (res.data.status === 'S-200') {
                    this.msg = '수정 완료되었습니다.';
                    this.$bvToast.toast(this.msg);
                }
            }).catch((error) => {
                console.log(error.response);
            });
        },
        userDelete() {
            this.$axios({
                method: 'delete',
                url: `/user/delete?uid=${this.uid}`,
            }).then((res) => {
                if (res.data.status === 'S-200') {
                    // 삭제 성공
                    this.msg = '정상적으로 탈퇴 처리되었습니다.';
                    this.$bvToast.toast(this.msg);
                }
            }).catch((error) => {
                console.log(error.response);
            });
        },
        onClickImageUpload() {
            this.$refs.imageInput.click();
        },
        onChangeImages(e) {
            console.log(e.target.files);
            if(e.target.files){
                const file = e.target.files[0];
                this.imageUrl = URL.createObjectURL(file);
            }
        }
    },
};
</script>

<style scoped>
.validation-fail-message {
  color: red;
}
</style>

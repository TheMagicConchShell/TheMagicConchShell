<template>
    <div>
        <link
            rel="stylesheet"
            href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css"
        >
        <h1>회원 정보</h1>

        <ValidationObserver ref="observer">
            <div>
                <p>
                    프로필 영역
                </p>
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
                <label for="introduce">자기소개</label>
                <textarea
                    id="introduce"
                    ref="introduce"
                    v-model="introduce"
                    type="text"
                    placeholder="자기소개"
                />
            </div>

            <div>
                <button @click="userUpdate">
                    정보 수정
                </button>
            </div>

            <div>
                <button @click="userDelete">
                    회원 탈퇴
                </button>
            </div>
        </ValidationObserver>
    </div>
</template>

<script>
import axios from 'axios';

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

extend('emailRegex', {
    validate(value) {
        return /^((?!\.)[\w-_.]*[^.])(@\w+)(\.\w+(\.\w+)?[^.\W])$/.test(value);
    },
    message: '이메일 형식이 일치하지 않습니다.',
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
        introduce: '',
        profile: '',
        passwordType: 'password',
    }),
    created() {
        this.uid = storage.getItem('login_user');
        axios({
            method: 'get',
            url: `http://localhost:8080/api/user/detail?uid=${this.uid}`,
        }).then((res) => {
            if (res.data.status) {
                this.email = res.data.object.email;
                this.nickname = res.data.object.nickname;
                this.introduce = res.data.object.introduce;
                this.profile = res.data.object.profile;
            }
        }).catch(() => {
            // 유저 정보 조회 실패
        });
    },
    methods: {
        async userUpdate() {
            const isValid = await this.$refs.observer.validate();

            if (!isValid) return;

            axios({
                method: 'put',
                url: 'http://localhost:8080/api/user/update',
                data: {
                    uid: this.uid,
                    email: this.email,
                    nickname: this.nickname,
                    password: this.password,
                    introduce: this.introduce,
                    profile: this.profile,
                },
            }).then((res) => {
                if (res.data.status) {
                    console.log(res.data);
                    this.email = res.data.object.email;
                    this.nickname = res.data.object.nickname;
                    this.introduce = res.data.object.introduce;
                } else {
                    // 닉네임 중복
                    console.log('duplicate nickname - update fail');
                }
            }).catch(() => {
                // 유저 정보 수정 실패
            });
        },
        userDelete() {
            axios({
                method: 'delete',
                url: `http://localhost:8080/api/user/delete?uid=${this.uid}`,
            }).then((res) => {
                if (res.data.status) {
                    // 삭제 성공
                }
            }).catch(() => {
                // 유저 정보 삭제 실패
            });
        },
    },
};
</script>

<style scoped>
.validation-fail-message {
  color: red;
}
</style>

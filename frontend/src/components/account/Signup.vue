<template>
    <div>
        Sign up
        <b-modal
            id="signup"
            title="회원 가입"
        >
            <div class="row">
                <div class="col-7">
                    <ValidationObserver ref="observer">
                        <!-- 이메일 -->
                        <div>
                            <ValidationProvider
                                v-slot="{ errors }"
                                class="input-wrap"
                                :rules="{required: true, emailRegex: true}"
                            >
                                <input
                                    id="email"
                                    ref="email"
                                    v-model="email"
                                    type="text"
                                    placeholder="아이디(이메일)"
                                >
                                <div
                                    v-if="errors[0]"
                                    class="validation-fail-message"
                                >
                                    {{ errors[0] }}
                                </div>
                            </validationprovider>
                        </div>

                        <!-- 닉네임 -->
                        <div>
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

                        <!-- 비밀번호 -->
                        <div>
                            <ValidationProvider
                                v-slot="{ errors }"
                                class="input-wrap password-wrap"
                                :rules="{
                                    required: true,
                                    min: 8,
                                    passwordRegex: true,
                                    passwordConfirm: passwordConfirm }"
                            >
                                <div id="passwordInput">
                                    <input
                                        id="password"
                                        ref="password"
                                        v-model="password"
                                        :type="passwordType"
                                        placeholder="비밀번호"
                                    >
                                    <i
                                        :class="{ 'fa fa-eye fa-lg' : passwordType === 'password' ,
                                                  'fa fa-eye-slash fa-lg' : passwordType === 'text' }"
                                        @click="passwordType === 'text'?
                                            passwordType = 'password': passwordType = 'text'"
                                    />
                                </div>
                                <div
                                    v-if="errors[0]"
                                    class="validation-fail-message"
                                >
                                    {{ errors[0] }}
                                </div>
                            </validationprovider>
                        </div>

                        <!-- 비밀번호 확인 -->
                        <div>
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
                    </ValidationObserver>
                </div>
            </div>
            <div class="float-right">
                <b-button
                    id="commonbutton"
                    @click.prevent="signup"
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

export default {
    name: 'Signin',
    components: {
        ValidationProvider,
        ValidationObserver,
    },
    data() {
        return {
            email: '',
            nickname: '',
            password: '',
            passwordConfirm: '',
            passwordType: 'password',
        };
    },
    methods: {
        async signup() {
            const isValid = await this.$refs.observer.validate();

            if (!isValid) {
                this.msg = '모든 항목을 형식에 맞게 입력해주세요.';
                this.$toast('안내', this.msg);
                return;
            }

            this.$store.dispatch('register', {
                email: this.email,
                nickname: this.nickname,
                password: this.password,
            })
                .then((res) => {
                    if (res.data.status === 'S-200') {
                        this.msg = '가입에 성공했습니다! 인증 메일이 발송되었습니다. 메일로 인증 후 로그인하여 주세요.';
                        this.$toast('안내', this.msg);
                        this.$bvModal.hide('signup');
                    }
                }).catch((error) => {
                    console.log(error.response);
                });
        },
    },
};
</script>

<style scoped>
#button {
    height: 100%;
    border: 0;
}
.validation-fail-message {
  color: red;
}
#passwordInput {
    display: flex;
    align-items: center;
}
#passwordInput i{
    position:absolute;
    right:20px;
}
</style>

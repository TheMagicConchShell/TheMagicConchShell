<template>
    <div>
        {{ $t('layout.regist') }}
        <b-modal
            id="signup"
            hide-header
            hide-footer
        >
            <div class="d-flex justify-content-center">
                <img
                    src="../../assets/images/welcome-sora.png"
                >
            </div>
            <ValidationObserver ref="observer">
                <!-- 이메일 -->
                <div>
                    <ValidationProvider
                        v-slot="{ errors }"
                        class="input-wrap"
                        :rules="{required: true, emailRegex: true}"
                    >
                        <div class="d-flex input">
                            <i class="far fa-envelope" />
                            <b-input
                                id="email"
                                ref="email"
                                v-model="email"
                                type="text"
                                placeholder="이메일"
                                :disabled="$wait.is('sign-up')"
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

                <!-- 닉네임 -->
                <div>
                    <ValidationProvider
                        v-slot="{ errors }"
                        class="input-wrap"
                        :rules="{required: true, max: 25}"
                    >
                        <div class="d-flex input">
                            <i class="fas fa-user-circle" />
                            <b-input
                                id="nickname"
                                ref="nickname"
                                v-model="nickname"
                                type="text"
                                placeholder="별명"
                                :disabled="$wait.is('sign-up')"
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
                        <div class="d-flex input">
                            <i class="fas fa-unlock" />
                            <b-input
                                id="password"
                                ref="password"
                                v-model="password"
                                :type="passwordType"
                                placeholder="비밀번호"
                                :disabled="$wait.is('sign-up')"
                            />
                            <i
                                id="password-show-eye"
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
                        <div class="d-flex input">
                            <i class="fas fa-lock" />
                            <b-input
                                id="passwordConfirm"
                                ref="passwordConfirm"
                                v-model="passwordConfirm"
                                :type="passwordType"
                                placeholder="비밀번호 확인"
                                :disabled="$wait.is('sign-up')"
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
            </ValidationObserver>
            <b-button
                block
                pill
                variant="outline-primary"
                class="center"
                :disabled="$wait.is('sign-up')"
                @click.prevent="signup"
            >
                회원 가입
            </b-button>
            <hr>
            <div class="center">
                <KakaoLogin
                    :disabled="$wait.is('sign-up')"
                />
            </div>
        </b-modal>
    </div>
</template>

<script>
import KakaoLogin from '@/components/account/KakaoLogin.vue';
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
    name: 'SignUp',
    components: {
        ValidationProvider,
        ValidationObserver,
        KakaoLogin,
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

            this.$wait.start('sign-up');
            this.$store.dispatch('register', {
                email: this.email,
                nickname: this.nickname,
                password: this.password,
            })
                .then((res) => {
                    this.msg = '가입에 성공했습니다! 인증 메일이 발송되었습니다. 메일로 인증 후 로그인하여 주세요.';
                    this.$toast('안내', this.msg);
                    this.$bvModal.hide('signup');
                }).catch((error) => {
                    this.$toast('안내', error.response.data.message);
                }).finally(() => {
                    this.$wait.end('sign-up');
                });
        },
    },
};
</script>

<style scoped>
.validation-fail-message {
  color: red;
  padding-left: 2rem;
}
#password-show-eye{
    position:absolute;
    right:20px;
}
i {
    flex: 1 1 30px;
}
img {
    max-width: 170px;
    height: auto;
}
.input {
    align-items: center;
}
.center {
    text-align: center;
    margin: 5px 0 5px 0;
}
</style>

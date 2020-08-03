<template>
    <div>
        <div class="worried-face">
            😟
        </div>
        <div class="code">
            {{ error.CODE }}
        </div>
        <div class="message">
            {{ error.MESSAGE }}
        </div>
    </div>
</template>

<script>
export default {
    props: {
        error: {
            type: Object,
            default: () => ({ 
                CODE: 'Error',
                MESSAGE: '오류가 발생했습니다',
            }),
        },
    },
};
</script>

<style scoped>
.worried-face {
    font-size: 60px;
}
.code {
    font-size: 60px;
    color: coral;
    text-decoration: underline;
}
.message {
    font-size: 36px;
}
</style>

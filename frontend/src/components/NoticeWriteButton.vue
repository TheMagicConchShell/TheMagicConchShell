<template>
    <button
        class="button"
        @click="handler"
    >
        {{ text }}
    </button>
</template>

<script>
export default {
    name: 'NoticeWriteButton',
    props: {
        handler: {
            type: Function,
            required: true,
        },
        text: {
            type: String,
            required: true,
        }
    }
};
</script>

<style>

</style>
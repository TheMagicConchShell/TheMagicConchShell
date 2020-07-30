<template>
    <base-editor
        :submit-url="'/support/notice'"
        :submit-method="'post'"
    />
</template>

<script>
import BaseEditor from '@/components/BaseEditor';

export default {
    name: 'NoticeEditor',
    components: {
        BaseEditor,
    },
};
</script>

<style>

</style>
<template>
    <form
        ref="form"
        @submit.prevent="submit"
    >
        <input
            v-model="title"
            type="text"
        >
        <input
            v-model="content"
            type="text"
        >
        <button type="submit">
            제출
        </button>
    </form>
</template>

<script>
export default {
    props: {
        submitUrl: {
            type: String,
            required: true,
        },
        submitMethod: {
            type: String,
            required: true,
        },
        defaultId: {
            type: Number,
            required: false,
            default: 0,
        },
        defaultWriter: {
            type: String,
            required: false,
            default: 'ssafy',
        },
        defaultTitle: {
            type: String,
            required: false,
            default: '',
        },
        defaultContent: {
            type: String,
            required: false,
            default: '',
        },
    },
    data: () => ({
        id: '',
        title: '',
        writer: '',
        content: '',
    }),
    watch: {
        defaultTitle() {
            this.id = this.defaultId;
            this.writer = this.defaultWriter;
            this.title = this.defaultTitle;
            this.content = this.defaultContent;
        },
    },
    methods: {
        async submit() {
            const response = await this.$axios({
                url: this.submitUrl,
                method: this.submitMethod,
                headers: {
                },
                data: {
                    nid: this.id,
                    title: this.title,
                    writer: this.writer,
                    content: this.content,
                },
            })
                .catch((error) => { console.log(error.response); });

            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.$toast('공지사항', '공지사항이 작성되었습니다');
                    this.$router.push({
                        name: 'NoticeList',
                    });
                } else {
                    console.log('글 작성이 실패하였습니다');
                }
            }

            // this.$emit('submit-done', {
            //     title: this.title,
            //     writer: this.writer,
            //     content: this.content,
            // });
        },
    },
};
</script>

<style>

</style>

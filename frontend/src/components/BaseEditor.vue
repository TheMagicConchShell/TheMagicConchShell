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
    data: () => ({
        title: "",
        writer: "",
        content: "",
    }),
    methods: {
        async submit(event) {
            const response = await this.$axios({
                url: `/api/support/notice`,
                method: "post",
                headers: {
                },
                data: {
                    nid: 1,
                    title: this.title,
                    writer: this.writer,
                    content: this.content,
                },
            })
                .catch((error) => {console.log(error.response);});

            if (response) {
                if (response.status >= 200 && response.status < 300) {
                    this.title = "";
                    this.content = "";
                    
                    console.log("글 작성이 완료되었습니다");
                } else {
                    console.log("글 작성이 완료되었습니다");
                }
            }

            // this.$emit('submit-done', {
            //     title: this.title,
            //     writer: this.writer,
            //     content: this.content,
            // });
        }
    },
};
</script>

<style>

</style>
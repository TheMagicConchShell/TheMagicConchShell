<template>
    <div>
        <b-button
            v-b-toggle.my-sidebar
            class="bg-light border-0"
        >
            <i
                class="fas fa-bars"
                style="color: black;"
            />
        </b-button>
        <b-sidebar
            id="my-sidebar"
            backdrop
            title="고양이 블로그"
            class="text-left"
        >
            <div class="px-3 py-2 border-top">
                <p>게시글</p>
                <p>이슈</p>
            </div>
            <template v-slot:footer="{ }">
                <router-link
                    class="d-flex border-top align-items-center px-3 py-2 text-decoration-none"
                    :to="{name: 'Home'}"
                >
                    <strong class="mr-auto">플랫폼 메인</strong>
                </router-link>
            </template>
        </b-sidebar>
    </div>
</template>

<script>
export default {

};
</script>

<style scoped>
#list {
    float: left;
}

#blognavimg {
    max-width: 80%;
    padding: 16px;
}
</style>

<template>
    <div>
        <b-button v-b-toggle.my-sidebar>
            <i class="fas fa-bars" />
        </b-button>
        <b-sidebar
            id="my-sidebar"
            shadow
        >
            <div class="px-3 py-2">
                <p>게시글</p>
                <p>이슈</p>
            </div>
        </b-sidebar>
    </div>
</template>

<script>
export default {

};
</script>

<style>
#list {
    float: left;
}
</style>

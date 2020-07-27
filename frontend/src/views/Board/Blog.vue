<template>
    <div>
        <div id="blogheader">
            <div id="blognav">
                <BlogNav />
            </div>
            <div
                id="title"
                class="d-flex justify-content-center"
            >
                고양이 블로그
            </div>
        </div>
        <div id="blogbody">
            <b-card
                title="Card Title"
                img-src="https://picsum.photos/600/300/?image=25"
                img-alt="Image"
                img-top
                tag="article"
                style="max-width: 100%;"
                class="mb-2"
            >
                <b-card-text>
                    임의의 글
                </b-card-text>

                <b-button
                    href="#"
                    variant="primary"
                >
                    Go somewhere
                </b-button>
            </b-card>
            <b-card
                title="Card Title"
                img-src="https://picsum.photos/600/300/?image=25"
                img-alt="Image"
                img-top
                tag="article"
                style="max-width: 100%;"
                class="mb-2"
            >
                <b-card-text>
                    임의의 글
                </b-card-text>

                <b-button
                    href="#"
                    variant="primary"
                >
                    Go somewhere
                </b-button>
            </b-card>
            <b-card
                title="Card Title"
                img-src="https://picsum.photos/600/300/?image=25"
                img-alt="Image"
                img-top
                tag="article"
                style="max-width: 100%;"
                class="mb-2"
            >
                <b-card-text>
                    임의의 글
                </b-card-text>

                <b-button
                    href="#"
                    variant="primary"
                >
                    Go somewhere
                </b-button>
            </b-card>
            <b-card
                title="Card Title"
                img-src="https://picsum.photos/600/300/?image=25"
                img-alt="Image"
                img-top
                tag="article"
                style="max-width: 20rem;"
                class="mb-2"
            >
                <b-card-text>
                    임의의 글
                </b-card-text>

                <b-button
                    href="#"
                    variant="primary"
                >
                    Go somewhere
                </b-button>
            </b-card>
            <b-card
                title="Card Title"
                img-src="https://picsum.photos/600/300/?image=25"
                img-alt="Image"
                img-top
                tag="article"
                style="max-width: 20rem;"
                class="mb-2"
            >
                <b-card-text>
                    임의의 글
                </b-card-text>

                <b-button
                    href="#"
                    variant="primary"
                >
                    Go somewhere
                </b-button>
            </b-card>
            <b-card
                title="Card Title"
                img-src="https://picsum.photos/600/300/?image=25"
                img-alt="Image"
                img-top
                tag="article"
                style="max-width: 20rem;"
                class="mb-2"
            >
                <b-card-text>
                    임의의 글
                </b-card-text>

                <b-button
                    href="#"
                    variant="primary"
                >
                    Go somewhere
                </b-button>
            </b-card>
        </div>
    </div>
</template>

<script>
import BlogNav from '../../components/BlogNav.vue';

export default {
    components: {
        BlogNav,
    },
};
</script>

<style scoped>
#blognav {
    float: left;
    top:50%
}
#blogbody {
    display:grid;
    grid-template-columns: repeat(3, 1fr);
    grid-gap: 20px;
    grid-auto-rows: minmax(200px, auto);
}

</style>

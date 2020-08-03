<template>
    <svg
        viewBox="0 0 24 24"
        class="icon"
    >
        <title>Close</title>
        <path
            d="M0 0h24v24H0V0z"
            fill="none"
        />
        <path
            fill="currentColor"
            d="M18.3 5.71c-.39-.39-1.02-.39-1.41 0L12 10.59 7.11 5.7c-.39-.39-1.02-.39-1.41 0-.39.39-.39 1.02 0 1.41L10.59 12 5.7 16.89c-.39.39-.39 1.02 0 1.41.39.39 1.02.39 1.41 0L12 13.41l4.89 4.89c.39.39 1.02.39 1.41 0 .39-.39.39-1.02 0-1.41L13.41 12l4.89-4.89c.38-.38.38-1.02 0-1.4z"
        />
    </svg>
</template>

<script>
export default {
    name: 'ButtonClose',
};
</script>

<style scoped>
.icon {
    color: gray;
    width: 1.65rem;
    height: 1.65rem;
    transform: translate(-4px, 0.8px);
}
</style>
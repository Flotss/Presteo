<template>
  <div>
    <Transition
      name="menu"
      enter-active-class="animate-wrapIn"
      leave-active-class="animate-wrapOut"
    >
      <slot v-if="showContent" />
    </Transition>
  </div>
</template>

<script setup>
defineProps({
  showContent: {
    type: Boolean,
    default: false,
  },
});
</script>

<style></style>

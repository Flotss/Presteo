<template>
  <div class="fixed inset-0 bg-white z-50 flex items-center justify-center">
    <div class="text-center">
      <div class="relative w-32 h-32 mx-auto mb-8">
        <!-- Outer Ring -->
        <div class="absolute inset-0 border-4 border-blue-200 rounded-full animate-pulse" />
        <!-- Middle Ring -->
        <div
          class="absolute inset-4 border-4 border-blue-400 rounded-full animate-spin"
          style="animation-duration: 2s"
        />
        <!-- Inner Ring -->
        <div
          class="absolute inset-8 border-4 border-blue-600 rounded-full animate-spin"
          style="animation-duration: 1.5s; animation-direction: reverse"
        />
        <!-- Center Icon -->
        <div class="absolute inset-0 flex items-center justify-center">
          <font-awesome-icon
            :icon="['fas', 'calendar-check']"
            class="h-12 w-12 text-blue-600 animate-bounce"
          />
        </div>
      </div>
      <h2 class="text-2xl font-bold text-gray-800 mb-2 animate-pulse">
        Loading Your Booking Experience
      </h2>
      <p class="text-gray-600">Please wait while we prepare everything...</p>
    </div>
  </div>
</template>

<style scoped>
@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.animate-spin {
  animation: spin 1s linear infinite;
}

.animate-pulse {
  animation: pulse 2s cubic-bezier(0.4, 0, 0.6, 1) infinite;
}

@keyframes pulse {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0.5;
  }
}

.animate-bounce {
  animation: bounce 1s infinite;
}

@keyframes bounce {
  0%,
  100% {
    transform: translateY(-25%);
    animation-timing-function: cubic-bezier(0.8, 0, 1, 1);
  }
  50% {
    transform: translateY(0);
    animation-timing-function: cubic-bezier(0, 0, 0.2, 1);
  }
}
</style> 
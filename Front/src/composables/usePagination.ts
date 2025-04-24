export const usePagination = <T>(
  items: Ref<T[]>,
  callBackFilter?: (item: T) => boolean,
  callbackPage?: () => void
) => {
  const currentPage = ref(1);
  const itemsPerPage = ref(10);
  const callbackPageInner = callbackPage ?? (() => {});

  const filteredItems = computed(() => {
    return  callBackFilter ? items.value.filter(callBackFilter) : items.value;
  });

  watch(filteredItems, () => {
      currentPage.value = 1;
  }, { immediate: true });



  const totalPages = computed(() => {
    return Math.max(1, Math.ceil(filteredItems.value.length / itemsPerPage.value));
  });

  const paginatedItems = computed(() => {
    const start = (currentPage.value - 1) * itemsPerPage.value;
    const end = start + itemsPerPage.value;
    return filteredItems.value.slice(start, end);
  });

  const nextPage = () => {
    if (currentPage.value < totalPages.value) {
      currentPage.value++;
      callbackPageInner();
    }
  };

  const prevPage = () => {
    if (currentPage.value > 1) {
      currentPage.value--;
      callbackPageInner();
    }
  };

  const goToPage = (page: number) => {
    if (page >= 1 && page <= totalPages.value) {
      currentPage.value = page;
      callbackPageInner();
    }
  };

  const resetPagination = () => {
    currentPage.value = 1;
    callbackPageInner();
  };

  return {
    paginatedItems,
    nextPage,
    prevPage,
    currentPage,
    resetPagination,
    goToPage,
    totalPages,
  };
};
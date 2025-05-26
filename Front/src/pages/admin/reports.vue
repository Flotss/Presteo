<template>
  <div class="max-w-5xl mx-auto p-8 bg-white rounded-lg shadow-md mt-12 mb-12">
    <h1 class="text-3xl font-extrabold text-center text-gray-900 mb-6">
      Reports Management
    </h1>
    <div class="mb-8 flex flex-col md:flex-row md:items-end md:space-x-4 space-y-4 md:space-y-0">
      <div class="flex-1">
        <label class="block text-gray-700 font-semibold mb-1">Filter by user (first or last name):</label>
        <input
          v-model="userFilter"
          type="text"
          placeholder="Type a name..."
          class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>
      <div class="flex-1">
        <label class="block text-gray-700 font-semibold mb-1">Filter by reported user ID:</label>
        <input
          v-model="userIdFilter"
          type="number"
          placeholder="User ID..."
          class="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>
      <button @click="fetchReports" class="bg-blue-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-blue-700 transition">Refresh</button>
    </div>

    <div class="mb-8 flex justify-between items-center">
      <h2 class="text-xl font-bold">Create a new report</h2>
      <button @click="showModal = true" class="bg-green-600 text-white px-6 py-3 rounded-lg font-semibold hover:bg-green-700 transition">
        Create new report
      </button>
    </div>

    <!-- Modal -->
    <div v-if="showModal" class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-40">
      <div class="bg-white rounded-lg shadow-lg p-8 w-full max-w-lg relative animate-fade-in">
        <button @click="closeModal" class="absolute top-2 right-2 text-gray-400 hover:text-gray-700 text-2xl font-bold">&times;</button>
        <h3 class="text-2xl font-bold mb-4 text-center">Create a new report</h3>
        <form @submit.prevent="createReport" class="flex flex-col space-y-4">
          <!-- Reported user autocomplete -->
          <div>
            <label class="block text-gray-700 font-semibold mb-1">Reported user</label>
            <input v-model="searchReported" @input="searchUsers('reported')" type="text" placeholder="Search by name, surname or ID..." class="p-3 border border-gray-300 rounded-lg w-full" autocomplete="off" />
            <ul v-if="showReportedResults && reportedResults.length" class="border border-gray-200 rounded-lg bg-white mt-1 max-h-40 overflow-auto z-10">
              <li v-for="user in reportedResults" :key="user.id" @click="selectUser(user, 'reported')" class="px-4 py-2 hover:bg-blue-100 cursor-pointer">
                {{ user.firstName }} {{ user.lastName }} (ID: {{ user.id }})
              </li>
            </ul>
            <div v-if="selectedReported" class="mt-1 text-green-700">Selected: {{ selectedReported.firstName }} {{ selectedReported.lastName }} (ID: {{ selectedReported.id }})</div>
          </div>
          <!-- Reporter user autocomplete -->
          <div>
            <label class="block text-gray-700 font-semibold mb-1">Reporter user</label>
            <input v-model="searchReporter" @input="searchUsers('reporter')" type="text" placeholder="Search by name, surname or ID..." class="p-3 border border-gray-300 rounded-lg w-full" autocomplete="off" />
            <ul v-if="showReporterResults && reporterResults.length" class="border border-gray-200 rounded-lg bg-white mt-1 max-h-40 overflow-auto z-10">
              <li v-for="user in reporterResults" :key="user.id" @click="selectUser(user, 'reporter')" class="px-4 py-2 hover:bg-blue-100 cursor-pointer">
                {{ user.firstName }} {{ user.lastName }} (ID: {{ user.id }})
              </li>
            </ul>
            <div v-if="selectedReporter" class="mt-1 text-green-700">Selected: {{ selectedReporter.firstName }} {{ selectedReporter.lastName }} (ID: {{ selectedReporter.id }})</div>
          </div>
          <textarea v-model="newReport.description" placeholder="Description" class="p-3 border border-gray-300 rounded-lg resize-none" required rows="3"></textarea>
          <div class="flex justify-end space-x-2">
            <button type="button" @click="closeModal" class="px-4 py-2 rounded bg-gray-200 hover:bg-gray-300 text-gray-700 font-semibold">Cancel</button>
            <button type="submit" class="px-6 py-2 rounded bg-green-600 hover:bg-green-700 text-white font-semibold flex items-center justify-center min-w-[100px]" :disabled="!selectedReported || !selectedReporter || creatingReport">
              <font-awesome-icon v-if="creatingReport" icon="circle-notch" spin class="mr-2" />
              <span v-if="!creatingReport">Create</span>
              <span v-else>Creating...</span>
            </button>
          </div>
        </form>
        <div v-if="createError" class="text-red-500 mt-2 text-center">{{ createError }}</div>
        <div v-if="createSuccess" class="text-green-600 mt-2 text-center">{{ createSuccess }}</div>
      </div>
    </div>

    <div v-if="loading" class="text-center text-gray-500">Loading reports...</div>
    <div v-if="error" class="text-center text-red-500">{{ error }}</div>
    <div v-if="filteredReports.length === 0 && !loading" class="text-center text-gray-500">No reports found.</div>
    <ul v-else class="divide-y divide-gray-200">
      <li v-for="report in filteredReports" :key="report.id" class="py-6 flex flex-col md:flex-row md:items-center md:justify-between">
        <div class="flex-1">
          <div class="flex flex-wrap items-center space-x-2 mb-2">
            <span class="font-semibold text-blue-700">Reported:</span>
            <span>{{ userName(report.userReported) }}</span>
            <span class="font-semibold text-gray-500">(ID: {{ report.userReported?.id }})</span>
            <span class="mx-2">|</span>
            <span class="font-semibold text-green-700">Reporter:</span>
            <span>{{ userName(report.userReporter) }}</span>
            <span class="font-semibold text-gray-500">(ID: {{ report.userReporter?.id }})</span>
          </div>
          <div class="text-gray-700">
            <span class="font-semibold">Description: </span>
            <span v-if="isCollapsed(report.id)">
              {{ shortDescription(report.description) }}<span v-if="report.description.length > maxDescLength">... <button class="text-blue-600 underline" @click="toggleCollapse(report.id)">See more</button></span>
            </span>
            <span v-else>
              {{ report.description }} <button class="text-blue-600 underline" @click="toggleCollapse(report.id)">See less</button>
            </span>
          </div>
        </div>
        <div class="mt-2 md:mt-0 md:ml-6 text-sm text-gray-500">
          <div>Created: {{ formatDate(report.createdAt) }}</div>
          <div>Updated: {{ formatDate(report.updatedAt) }}</div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { computed, ref, watch } from 'vue';

definePageMeta({
  layout: 'admin',
  middleware: 'auth',
});

const reports = ref([]);
const loading = ref(false);
const error = ref('');
const userFilter = ref('');
const userIdFilter = ref('');
const collapsed = ref({});

const showModal = ref(false);

const newReport = ref({
  userReportedId: '',
  userReporterId: '',
  description: '',
});
const createError = ref('');
const createSuccess = ref('');

// Autocomplete state
const searchReported = ref('');
const searchReporter = ref('');
const reportedResults = ref([]);
const reporterResults = ref([]);
const showReportedResults = ref(false);
const showReporterResults = ref(false);
const selectedReported = ref(null);
const selectedReporter = ref(null);

// Debounce timeouts
let reportedTimeout = null;
let reporterTimeout = null;

const maxDescLength = 80;

const creatingReport = ref(false);

function userName(user) {
  if (!user) return '';
  return [user.firstName, user.lastName].filter(Boolean).join(' ');
}

function shortDescription(desc) {
  if (!desc) return '';
  return desc.length > maxDescLength ? desc.slice(0, maxDescLength) : desc;
}

function isCollapsed(id) {
  return collapsed.value[id] !== false;
}

function toggleCollapse(id) {
  collapsed.value[id] = !isCollapsed(id);
}

function formatDate(dateStr) {
  if (!dateStr) return '';
  return new Date(dateStr).toLocaleString();
}

function closeModal() {
  showModal.value = false;
  createError.value = '';
  createSuccess.value = '';
  newReport.value = { userReportedId: '', userReporterId: '', description: '' };
}

const filteredReports = computed(() => {
  return reports.value.filter((r) => {
    const matchesUserId = !userIdFilter.value || (r.userReported && r.userReported.id == userIdFilter.value);
    const matchesName = !userFilter.value || 
      [r.userReported, r.userReporter].some(user => {
        if (!user) return false;
        const searchTerm = userFilter.value.toLowerCase();
        const firstName = user.firstName?.toLowerCase() || '';
        const lastName = user.lastName?.toLowerCase() || '';
        return firstName.includes(searchTerm) || lastName.includes(searchTerm);
      });
    return matchesUserId && matchesName;
  });
});

async function fetchReports() {
  loading.value = true;
  error.value = '';
  try {
    let url = 'reports';
    if (userIdFilter.value) {
      url = `reports/${userIdFilter.value}`;
    }
    const { data, error: fetchError, fetch } = useApi(url);
    await fetch();
    if (fetchError.value) throw new Error(fetchError.value.message);
    reports.value = data.value || [];
  } catch (err) {
    error.value = err.message || 'Failed to fetch reports.';
  } finally {
    loading.value = false;
  }
}

async function createReport() {
  createError.value = '';
  createSuccess.value = '';
  creatingReport.value = true;
  try {
    const { data, error: postError, post } = await useApi('reports/create');
    await post({
      userReportedId: newReport.value.userReportedId,
      userReporterId: newReport.value.userReporterId,
      description: newReport.value.description,
    });
    if (postError.value) throw new Error(postError.value.message);
    createSuccess.value = 'Report created successfully!';
    setTimeout(closeModal, 1200);
    fetchReports();
  } catch (err) {
    createError.value = err.message || 'Failed to create report.';
  } finally {
    creatingReport.value = false;
  }
}

async function searchUsers(type) {
  const query = type === 'reported' ? searchReported.value : searchReporter.value;
  if (!query || query.length < 2) {
    if (type === 'reported') reportedResults.value = [];
    if (type === 'reporter') reporterResults.value = [];
    return;
  }
  // Debounce: clear previous timeout
  if (type === 'reported' && reportedTimeout) {
    clearTimeout(reportedTimeout);
  }
  if (type === 'reporter' && reporterTimeout) {
    clearTimeout(reporterTimeout);
  }
  const doSearch = async () => {
    try {
      const { data, fetch } = useApi(`users/search?query=${encodeURIComponent(query)}`);
      await fetch();
      if (type === 'reported') {
        reportedResults.value = data.value || [];
        showReportedResults.value = true;
      } else {
        reporterResults.value = data.value || [];
        showReporterResults.value = true;
      }
    } catch (e) {
      if (type === 'reported') reportedResults.value = [];
      if (type === 'reporter') reporterResults.value = [];
    }
  };
  if (type === 'reported') {
    reportedTimeout = setTimeout(doSearch, 300);
  } else {
    reporterTimeout = setTimeout(doSearch, 300);
  }
}

function selectUser(user, type) {
  if (type === 'reported') {
    selectedReported.value = user;
    newReport.value.userReportedId = user.id;
    showReportedResults.value = false;
    searchReported.value = `${user.firstName} ${user.lastName} (${user.id})`;
  } else {
    selectedReporter.value = user;
    newReport.value.userReporterId = user.id;
    showReporterResults.value = false;
    searchReporter.value = `${user.firstName} ${user.lastName} (${user.id})`;
  }
}

watch(showModal, (val) => {
  if (val) {
    searchReported.value = '';
    searchReporter.value = '';
    reportedResults.value = [];
    reporterResults.value = [];
    selectedReported.value = null;
    selectedReporter.value = null;
    newReport.value = { userReportedId: '', userReporterId: '', description: '' };
  }
});

onMounted(fetchReports);
</script>

<style scoped>
@keyframes fade-in {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
.animate-fade-in {
  animation: fade-in 0.2s ease;
}
</style> 
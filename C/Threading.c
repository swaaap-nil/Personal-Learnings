#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define NUM_THREADS 26

// Function to be executed by each thread
void *threadFunction(void *threadId) {
    long tid = (long)threadId;
    printf("Thread %ld: Hello, World!\n", tid);
    pthread_exit(NULL);
}

int main() {
    pthread_t threads[NUM_THREADS];
    int rc;
    long t;

    // Create multiple threads
    for (t = 0; t < NUM_THREADS; t++) {
        printf("Creating thread %ld\n", t);
        rc = pthread_create(&threads[t], NULL, threadFunction, (void *)t);
        if (rc) {
            printf("Error: Unable to create thread, %d\n", rc);
            exit(-1);
        }
    }

    // Wait for all threads to finish
    for (t = 0; t < NUM_THREADS; t++) {
        pthread_join(threads[t], NULL);
    }

    printf("All threads have completed execution.\n");

    pthread_exit(NULL);
}
package com.zozx.okvolley;

/**
 * Created by zozx on 17/1/5.
 * http retry policy.
 */
public class HttpRetryPolicy {

    private int initialTimeoutMs;
    private int maxNumRetries;
    private float backoffMultiplier;

    /**
     * The default socket timeout in milliseconds
     */
    public static final int DEFAULT_TIMEOUT_MS = 2500;

    /**
     * The default number of retries
     */
    public static final int DEFAULT_MAX_RETRIES = 1;

    /**
     * The default backoff multiplier
     */
    public static final float DEFAULT_BACKOFF_MULT = 1f;

    /**
     * Constructs a new retry policy using the default timeouts.
     */
    public HttpRetryPolicy() {
        this(DEFAULT_TIMEOUT_MS, DEFAULT_MAX_RETRIES, DEFAULT_BACKOFF_MULT);
    }

    /**
     * Constructs a new retry policy.
     *
     * @param initialTimeoutMs  The initial timeout for the policy.
     * @param maxNumRetries     The maximum number of retries.
     * @param backoffMultiplier Backoff multiplier for the policy.
     */
    public HttpRetryPolicy(int initialTimeoutMs, int maxNumRetries, float backoffMultiplier) {
        this.initialTimeoutMs = initialTimeoutMs;
        this.maxNumRetries = maxNumRetries;
        this.backoffMultiplier = backoffMultiplier;
    }

    /**
     * @return The initial timeout milliseconds.
     */
    public int getInitialTimeoutMs() {
        return initialTimeoutMs;
    }

    /**
     * @return The maximum number of retries.
     */
    public int getMaxNumRetries() {
        return maxNumRetries;
    }

    /**
     * @return Backoff multiplier for the policy.
     */
    public float getBackoffMultiplier() {
        return backoffMultiplier;
    }
}

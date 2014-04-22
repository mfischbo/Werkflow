package de.artignition.werkflow.engine;

import de.artignition.werkflow.repository.JobInstanceRepository;

import org.springframework.context.ApplicationContext;

/**
 * Factory class to create a {@link JobInstanceExecutor}
 * @author M.Fischboeck 
 *
 */
public class JobInstanceExecutorFactory {

	/**
	 * Creates a new JobInstanceExecutor
	 * @param ctx The ApplicationContext to pull beans from
	 * @return The JobInstanceExecutor
	 */
	public static JobInstanceExecutor newInstance(ApplicationContext ctx) {
		JobInstanceRepository repo = ctx.getBean(JobInstanceRepository.class);
		JobInstanceExecutor i = new JobInstanceExecutor(ctx, repo);
		return i;
	}
}

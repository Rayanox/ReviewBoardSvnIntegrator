package upgrade.karavel.services.reviewBoardSvnIntegrator.features.managers;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.FileSystemUtils;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.applications.Application;
import upgrade.karavel.services.reviewBoardSvnIntegrator.dao.commit.Branch;
import upgrade.karavel.services.reviewBoardSvnIntegrator.services.SvnService;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
@Log4j2
public class WorkspaceManager {

    private final File rootWorkspaceDir;
    private final SvnService svnService;

    private static final HashMap<String, File> mapWorkspaceFileByAppBranch = new HashMap<>();

    public void initWorkspaceDirs(List<Application> applications) {
        applications.forEach(this::createApplicationWorkspace);
    }

    public void createBranchWorkspaceDir(Branch branch) {
        File branchDir = getBranchWorkspaceFile(branch);
        Assert.notNull(branchDir, "Functional error -> Branch File is null and should not be");
        branchDir.mkdirs();
        svnService.checkoutRepo(branch, branchDir);
    }

    public Path getApplicationPath(Application application) {
        return Paths.get(rootWorkspaceDir.getPath(), application.getName());
    }

    private Path getBranchPath(Branch branch) {
        return Paths.get(getApplicationPath(branch.getApplication()).toString(), branch.getBranchName());
    }

    public File getBranchWorkspaceFile(Branch branch) {
        String keyBranchApp = String.format("%s-%s", branch.getApplication().getName(), branch.getBranchName());

        File workspaceAppFile = mapWorkspaceFileByAppBranch.get(keyBranchApp);
        if(Objects.isNull(workspaceAppFile)) {
            workspaceAppFile = getBranchPath(branch).toFile();
            mapWorkspaceFileByAppBranch.put(keyBranchApp, workspaceAppFile);
        }

        return workspaceAppFile;
    }


    /*
        Privates
     */

    private void createApplicationWorkspace(Application application) {
        File workspaceAppFolder = getApplicationPath(application).toFile();
        workspaceAppFolder.mkdirs();
    }

    public void deleteBranchWorkspaceDir(Branch branch) {
        File folderWorkspace = getBranchWorkspaceFile(branch);
        boolean deleted = FileSystemUtils.deleteRecursively(folderWorkspace);
        if(!deleted)
            log.error("Folder of branch {}({}) could not be deleted...", branch.getBranchName(), branch.getApplication().getName());
    }
}

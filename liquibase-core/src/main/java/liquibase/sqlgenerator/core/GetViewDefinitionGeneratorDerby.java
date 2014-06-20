package liquibase.sqlgenerator.core;

import liquibase.CatalogAndSchema;
import liquibase.action.Action;
import liquibase.action.UnparsedSql;
import liquibase.actiongenerator.ActionGeneratorChain;
import liquibase.database.core.DerbyDatabase;
import liquibase.executor.ExecutionOptions;
import liquibase.statement.core.GetViewDefinitionStatement;

public class GetViewDefinitionGeneratorDerby extends GetViewDefinitionGenerator {
    @Override
    public int getPriority() {
        return PRIORITY_DATABASE;
    }

    @Override
    public boolean supports(GetViewDefinitionStatement statement, ExecutionOptions options) {
        return options.getRuntimeEnvironment().getTargetDatabase() instanceof DerbyDatabase;
    }

    @Override
    public Action[] generateActions(GetViewDefinitionStatement statement, ExecutionOptions options, ActionGeneratorChain chain) {
        CatalogAndSchema schema = new CatalogAndSchema(statement.getCatalogName(), statement.getSchemaName()).customize(options.getRuntimeEnvironment().getTargetDatabase());

        return new Action[] {
                    new UnparsedSql("select V.VIEWDEFINITION from SYS.SYSVIEWS V, SYS.SYSTABLES T, SYS.SYSSCHEMAS S WHERE  V.TABLEID=T.TABLEID AND T.SCHEMAID=S.SCHEMAID AND T.TABLETYPE='V' AND T.TABLENAME='" + statement.getViewName() + "' AND S.SCHEMANAME='"+schema.getSchemaName()+"'")
            };
    }
}